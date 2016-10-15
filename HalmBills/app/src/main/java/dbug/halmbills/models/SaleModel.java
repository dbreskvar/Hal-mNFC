package dbug.halmbills.models;

public class SaleModel {

    String transactionid, orderid, channelid;
    Integer paymenttokennumber, status;
    AuthModel auth;

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    public Integer getPaymenttokennumber() {
        return paymenttokennumber;
    }

    public void setPaymenttokennumber(Integer paymenttokennumber) {
        this.paymenttokennumber = paymenttokennumber;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public AuthModel getAuth() {
        return auth;
    }

    public void setAuth(AuthModel auth) {
        this.auth = auth;
    }
}
