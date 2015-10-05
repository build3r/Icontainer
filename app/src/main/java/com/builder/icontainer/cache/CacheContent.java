package com.builder.icontainer.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class CacheContent
{

    /**
     * An array of sample (dummy) items.
     */
    public static List<CacheItems> ITEMS = new ArrayList<CacheItems>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, CacheItems> ITEM_MAP = new HashMap<String, CacheItems>();

    static
    {
        // Add 3 sample items.
        addItem(new CacheItems("1", "rice","",1.2f,1440768237));
        addItem(new CacheItems("2", "sugar","",1.2f,1440768237));
        addItem(new CacheItems("3", "salt","",1.2f,1440768237));
    }

    private static void addItem(CacheItems item)
    {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class CacheItems
    {


        public String id;
        public String content;
        public String image_uri;
        public float curent_weigth;
        public long refilled_date;

        public CacheItems(String id, String content, String image_uri, float curent_weigth, long refilled_date)
        {
            this.id = id;
            this.content = content;
            this.image_uri = image_uri;
            this.curent_weigth = curent_weigth;
            this.refilled_date = refilled_date;
        }



        @Override
        public String toString()
        {
            return content;
        }
    }
}
