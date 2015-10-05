package com.builder.icontainer.models;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Container {

@Expose
private long containerId;
@Expose
private String item;
@Expose
private List<Float> itemWeight = new ArrayList<Float>();
@Expose
private List<Long> date = new ArrayList<Long>();

    /**
     * Returns a string containing a concise, human-readable description of this
     * object. Subclasses are encouraged to override this method and provide an
     * implementation that takes into account the object's type and data. The
     * default implementation is equivalent to the following expression:
     * <pre>
     *   getClass().getName() + '@' + Integer.toHexString(hashCode())</pre>
     * <p>See <a href="{@docRoot}reference/java/lang/Object.html#writing_toString">Writing a useful
     * {@code toString} method</a>
     * if you intend implementing your own {@code toString} method.
     *
     * @return a printable representation of this object.
     */
    @Override
    public String toString()
    {
        return "Id : = "+containerId+"  ITEM = "+item+"  Item weiight List = "+itemWeight.toString()+"  date = "+date.toString();
    }

    /**
* 
* @return
* The containerId
*/
public long getContainerId() {
return containerId;
}

/**
* 
* @param containerId
* The containerId
*/
public void setContainerId(long containerId) {
this.containerId = containerId;
}

/**
* 
* @return
* The item
*/
public String getItem() {
return item;
}

/**
* 
* @param item
* The item
*/
public void setItem(String item) {
this.item = item;
}

/**
* 
* @return
* The itemWeight
*/
public List<Float> getItemWeight() {
return itemWeight;
}

/**
* 
* @param itemWeight
* The itemWeight
*/
public void setItemWeight(List<Float> itemWeight) {
this.itemWeight = itemWeight;
}

/**
* 
* @return
* The date
*/
public List<Long> getDate() {
return date;
}

/**
* 
* @param date
* The date
*/
public void setDate(List<Long> date) {
this.date = date;
}

}



