package dbug.halmbills.models;

public class AuthModel {

    Integer nonce;
    String timestamp, signature;

    public Integer getNonce() {
        return nonce;
    }

    public void setNonce(Integer nonce) {
        this.nonce = nonce;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
