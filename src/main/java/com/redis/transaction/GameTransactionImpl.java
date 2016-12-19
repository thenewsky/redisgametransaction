package com.redis.transaction;

import com.redis.transaction.entity.TEntity;
import com.redis.transaction.enums.CommitResult;
import com.redis.transaction.exception.TException;
import com.redis.util.TimeUtil;

/**
 * Created by jiangwenping on 16/12/6.
 */
public class GameTransactionImpl extends AbstractGameTransaction {

    private long waitTime;

    public GameTransactionImpl(String cause){
        super(cause);
    }

    public GameTransactionImpl(String cause, long waitTime){
        super(cause);
        this.waitTime = waitTime;
    }


    public void commit() throws TException {
        if(state != TRY_COMMITED)
            throw new TException();
        this.state = COMMITED;
        for(TEntity entity:entities){
            if(!entity.needCommit()){
                continue;
            }
            entity.commit();
        }
    }

    public void rollback() throws TException {
        //无条件进行回滚
        this.state = ROLLBACK;
        for(TEntity entity:entities){
            entity.rollback();
        }
    }


    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[PLAYERTRANSACTION]");
        for(TEntity entity:entities){
            sb.append(entity.toString());
        }
        return sb.toString();
    }

    @Override
    public void trycommit() throws TException {
        if (state != ACTIVE)
            throw new TException("GameTransactionImpl is active, can't trycommit");
        this.state = TRY_COMMITED;
        for(TEntity entity:entities){
            if(!entity.needCommit()){
                continue;
            }
            CommitResult gameTransactionEntityTryCommitResult = entity.trycommit();
            if(!gameTransactionEntityTryCommitResult.equals(CommitResult.SUCCESS)){
                this.gameTransactionTryCommitResult = gameTransactionEntityTryCommitResult;
                break;
            }
        }
    }


    @Override
    public boolean createGameTransactionLock()  throws TException {
        if(state != ACTIVE)
            throw new TException();
        boolean creatflag = true;
        long startSecond = TimeUtil.getSeconds();
        if(waitTime > 0){
            for(;;){
                long currSeconds = TimeUtil.getSeconds();
                creatflag = createOnceGameTransactionLock(currSeconds);
                if(creatflag = true){
                    break;
                }

                try {
                    Thread.sleep(TimeUtil.SECOND);
                }catch (Throwable e){

                }

                currSeconds = TimeUtil.getSeconds();
                if(startSecond + waitTime < currSeconds){
                    creatflag = false;
                    break;
                }
            }
        }else {
            long seconds = TimeUtil.getSeconds();
            creatflag = createOnceGameTransactionLock(startSecond);
        }
        return creatflag;
    }

    public boolean createOnceGameTransactionLock(long seconds)throws TException {
        boolean creatFlag = false;
        for (TEntity entity : entities) {
            try {
                creatFlag = entity.createLock(seconds);
            } catch (Exception e) {
                throw new TException(e.getMessage());
            }
            if (!creatFlag) {
                break;
            }
        }
        return creatFlag;
    }


    @Override
    public void releaseGameTransactionLock() {
        for(TEntity entity:entities){
            entity.release();
        }
    }

    /**
     * 获取事务信息
     * @return
     */
    public String getTransactionInfo(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("transaction ");
        buffer.append(getCause());
        buffer.append(":");
        for(int i = 0; i < entities.size(); i++){
            TEntity entity = entities.get(i);
            buffer.append(entity.getInfo());
            if(i < entities.size()-1){
                buffer.append(",");
            }
        }
        return buffer.toString();
    }
}


