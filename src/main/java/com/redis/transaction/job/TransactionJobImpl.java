package com.redis.transaction.job;

import com.redis.log.Loggers;
import com.redis.transaction.TransactionImpl;
import com.redis.transaction.job.entity.TJobEntity;
import com.redis.transaction.enums.CommitResult;
import com.redis.transaction.exception.TException;
import org.slf4j.Logger;


/**
 * 用于调度事务
 */
public class TransactionJobImpl implements TransactionJob {


    protected final Logger logger = Loggers.transactionLogger;

    public CommitResult commit(String jobName, TJobEntity... entities) {
        CommitResult result = CommitResult.SUCCESS;
        TransactionImpl transaction = new TransactionImpl(jobName);
        for (int i = 0; i < entities.length; i++) {
            transaction.addEntity(entities[i]);
        }
        try {
            if (transaction.lockAll()) {
                logger.info("获得" + transaction + "锁成功");
                transaction.trycommit();
                if (transaction.canCommit()) {
                    logger.info("提交" + transaction + "事务");
                    transaction.commit();
                    logger.info("提交" + transaction + "事务成功");
                } else {//不可以提交
                    result = transaction.getCommitResult();
                    logger.info("尝试提交" + transaction + "事务失败");
                }

            } else {
                logger.info("获得" + transaction + "锁失败");
                result = CommitResult.LOCK_ERROR;
            }
        } catch (Exception e) {
            logger.error("提交" + transaction + "锁异常", e);
            try {
                transaction.rollback();
            } catch (Exception e2) {
                logger.error("回滚" + transaction + "锁异常", e);
            }
            result = CommitResult.COMMON_ERROR;
            if (e instanceof TException) {
                TException gameTransactionException = (TException) e;
                CommitResult tempGameTransactionTryCommitResult = gameTransactionException.getCommitResult();
                if (tempGameTransactionTryCommitResult != null) {
                    result = tempGameTransactionTryCommitResult;
                }
            }
        } finally {
            logger.info("释放" + transaction + "锁");
            transaction.unlockAll();
        }

        return result;
    }


    public CommitResult commit(String jobName, long waitTime, TJobEntity... jobEntitys) {
        CommitResult gameTransactionTryCommitResult = CommitResult.SUCCESS;
        TransactionImpl gameTransaction = new TransactionImpl(jobName, waitTime);
        for (int i = 0; i < jobEntitys.length; i++) {
            gameTransaction.addEntity(jobEntitys[i]);
        }
        try {
            if (gameTransaction.lockAll()) {
                logger.info("获得" + gameTransaction + "锁成功");
                gameTransaction.trycommit();
                if (gameTransaction.canCommit()) {
                    logger.info("提交" + gameTransaction + "事务");
                    gameTransaction.commit();
                    logger.info("提交" + gameTransaction + "事务成功");
                } else {//不可以提交
                    gameTransactionTryCommitResult = gameTransaction.getCommitResult();
                    logger.info("尝试提交" + gameTransaction + "事务失败");
                }

            } else {
                logger.info("获得" + gameTransaction + "锁失败");
                gameTransactionTryCommitResult = CommitResult.LOCK_ERROR;
            }
        } catch (Exception e) {
            logger.error("提交" + gameTransaction + "锁异常", e);
            try {
                gameTransaction.rollback();
            } catch (Exception e2) {
                logger.error("回滚" + gameTransaction + "锁异常", e);
            }
            gameTransactionTryCommitResult = CommitResult.COMMON_ERROR;
            if (e instanceof TException) {
                TException gameTransactionException = (TException) e;
                CommitResult tempGameTransactionTryCommitResult = gameTransactionException.getCommitResult();
                if (tempGameTransactionTryCommitResult != null) {
                    gameTransactionTryCommitResult = tempGameTransactionTryCommitResult;
                }
            }
        } finally {
            logger.info("释放" + gameTransaction + "锁");
            gameTransaction.unlockAll();
        }

        return gameTransactionTryCommitResult;
    }


}

