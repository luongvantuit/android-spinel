package org.rogo.spinel.ncp;
import org.rogo.spinel.helpers.Utilities;

public class FrameData {

    private int propertyId;
    private int tid;
    private byte[] propertyValue;
    private Object response;
    private int uid;

    public FrameData() {
    }

    public FrameData(int propertyId, byte tid, byte[] propertyValue, Object response) {
        this.propertyId = propertyId;
        this.propertyValue = propertyValue;
        this.tid = tid;
        this.response = response;
        this.uid = Utilities.getUid(propertyId, tid);
    }

    public int getUid() {
        return uid;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public byte[] getPropertyValue() {
        return propertyValue;
    }

    public Object getResponse() {
        return response;
    }

    public int getTid() {
        return tid;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public void setPropertyValue(byte[] propertyValue) {
        this.propertyValue = propertyValue;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

}
