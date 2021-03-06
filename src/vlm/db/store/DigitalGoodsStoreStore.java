package vlm.db.store;

import vlm.DigitalGoodsStore;
import vlm.crypto.EncryptedData;
import vlm.db.DbIterator;
import vlm.db.DbKey;
import vlm.db.VersionedEntityTable;
import vlm.db.VersionedValuesTable;


public interface DigitalGoodsStoreStore {

    DbKey.LongKeyFactory<DigitalGoodsStore.Purchase> getFeedbackDbKeyFactory();

    DbKey.LongKeyFactory<DigitalGoodsStore.Purchase> getPurchaseDbKeyFactory();

    VersionedEntityTable<DigitalGoodsStore.Purchase> getPurchaseTable();

    VersionedValuesTable<DigitalGoodsStore.Purchase, EncryptedData> getFeedbackTable();

    DbKey.LongKeyFactory<DigitalGoodsStore.Purchase> getPublicFeedbackDbKeyFactory();

    VersionedValuesTable<DigitalGoodsStore.Purchase, String> getPublicFeedbackTable();

    DbKey.LongKeyFactory<DigitalGoodsStore.Goods> getGoodsDbKeyFactory();

    VersionedEntityTable<DigitalGoodsStore.Goods> getGoodsTable();

    DbIterator<DigitalGoodsStore.Goods> getGoodsInStock(int from, int to);

    DbIterator<DigitalGoodsStore.Goods> getSellerGoods(long sellerId, boolean inStockOnly, int from, int to);

    DbIterator<DigitalGoodsStore.Purchase> getAllPurchases(int from, int to);

    DbIterator<DigitalGoodsStore.Purchase> getSellerPurchases(long sellerId, int from, int to);

    DbIterator<DigitalGoodsStore.Purchase> getBuyerPurchases(long buyerId, int from, int to);

    DbIterator<DigitalGoodsStore.Purchase> getSellerBuyerPurchases(long sellerId, long buyerId, int from, int to);

    DbIterator<DigitalGoodsStore.Purchase> getPendingSellerPurchases(long sellerId, int from, int to);

    DbIterator<DigitalGoodsStore.Purchase> getExpiredPendingPurchases(int timestamp);
}
