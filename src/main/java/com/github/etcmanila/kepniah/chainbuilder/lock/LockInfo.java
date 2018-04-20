package com.github.etcmanila.kepniah.chainbuilder.lock;

public class LockInfo {
    private long timeout;
    private String lockKey;
    public long getTimeout() {
        return timeout;
    }
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
    public String getLockKey() {
        return lockKey;
    }
    public void setLockKey(String lockKey) {
        this.lockKey = lockKey;
    }
    
}
