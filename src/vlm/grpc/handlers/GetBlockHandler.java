package vlm.grpc.handlers;

import vlm.Block;
import vlm.Blockchain;
import vlm.grpc.GrpcApiHandler;
import vlm.grpc.proto.ApiException;
import vlm.grpc.proto.ProtoBuilder;
import vlm.grpc.proto.VlmApi;
import vlm.services.BlockService;

public class GetBlockHandler implements GrpcApiHandler<VlmApi.GetBlockRequest, VlmApi.Block> {

    private final Blockchain blockchain;
    private final BlockService blockService;

    public GetBlockHandler(Blockchain blockchain, BlockService blockService) {
        this.blockchain = blockchain;
        this.blockService = blockService;
    }

    @Override
    public VlmApi.Block handleRequest(VlmApi.GetBlockRequest request) throws Exception {
        long blockId = request.getId();
        int blockHeight = request.getHeight();
        int timestamp = request.getTimestamp();

        Block block;
        if (blockId > 0) {
            try {
                block = blockchain.getBlock(blockId);
            } catch (RuntimeException e) {
                throw new ApiException("Incorrect Block ID");
            }
        } else if (blockHeight > 0) {
            try {
                if (blockHeight > blockchain.getHeight()) {
                    throw new ApiException("Incorrect Block Height");
                }
                block = blockchain.getBlockAtHeight(blockHeight);
            } catch (RuntimeException e) {
                throw new ApiException("Incorrect Block Height");
            }
        } else if (timestamp > 0) {
            try {
                block = blockchain.getLastBlock(timestamp);
            } catch (RuntimeException e) {
                throw new ApiException("Incorrect Timestamp");
            }
        } else {
            block = blockchain.getLastBlock();
        }

        if (block == null) {
            throw new ApiException("Unknown Block");
        }

        boolean includeTransactions = request.getIncludeTransactions();

        return ProtoBuilder.buildBlock(blockchain, blockService, block, includeTransactions);
    }
}
