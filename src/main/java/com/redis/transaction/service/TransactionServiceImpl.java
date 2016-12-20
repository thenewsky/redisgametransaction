package com.redis.transaction.service;

import com.redis.log.Loggers;
import com.redis.transaction.TransactionImpl;
import com.redis.transaction.entity.AbstractTEntity;
import com.redis.transaction.enums.CommitResult;
import com.redis.transaction.exception.TException;
import org.slf4j.Logger;


/**
 * Created by jiangwenping on 16/12/6.
 */
public class TransactionServiceImpl implements TransactionService {


    protected final Logger logger = Loggers.transactionLogger;

    public CommitResult commitTransaction(String transctionName, AbstractTEntity... entities) {
        CommitResult result = CommitResult.SUCCESS;
        TransactionImpl transaction = new TransactionImpl(transctionName);
        for (int i = 0; i < entities.length; i++) {
            transaction.addEntity(entities[i]);
        }
        try {
            if (transaction.lockAll()) {
                logger.info("获得" + transaction.getTransactionInfo() + "锁成功");
                transaction.trycommit();
                if (transaction.canCommit()) {
                    logger.info("提交" + transaction.getTransactionInfo() + "事务");
                    transaction.commit();
                    logger.info("提交" + transaction.getTransactionInfo() + "事务成功");
                } else {//不可以提交
                    result = transaction.getGameTransactionTryCommitResult();
                    logger.info("尝试提交" + transaction.getTransactionInfo() + "事务失败");
                }

            } else {
                logger.info("获得" + transaction.getTransactionInfo() + "锁失败");
                result = CommitResult.LOCK_ERROR;
            }
        } catch (Exception e) {
            logger.error("提交" + transaction.getTransactionInfo() + "锁异常", e);
            try {
                transaction.rollback();
            } catch (Exception e2) {
                logger.error("回滚" + transaction.getTransactionInfo() + "锁异常", e);
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
            logger.info("释放" + transaction.getTransactionInfo() + "锁");
            transaction.unlockAll();
        }

        return result;
    }


    public CommitResult commitTransaction(String gameTransactionCause, long waitTime, AbstractTEntity... abstractGameTransactionEntity) {
        CommitResult gameTransactionTryCommitResult = CommitResult.SUCCESS;
        TransactionImpl gameTransaction = new TransactionImpl(gameTransactionCause, waitTime);
        for (int i = 0; i < abstractGameTransactionEntity.length; i++) {
            gameTransaction.addEntity(abstractGameTransactionEntity[i]);
        }
        try {
            if (gameTransaction.lockAll()) {
                logger.info("获得" + gameTransaction.getTransactionInfo() + "锁成功");
                gameTransaction.trycommit();
                if (gameTransaction.canCommit()) {
                    logger.info("提交" + gameTransaction.getTransactionInfo() + "事务");
                    gameTransaction.commit();
                    logger.info("提交" + gameTransaction.getTransactionInfo() + "事务成功");
                } else {//不可以提交
                    gameTransactionTryCommitResult = gameTransaction.getGameTransactionTryCommitResult();
                    logger.info("尝试提交" + gameTransaction.getTransactionInfo() + "事务失败");
                }

            } else {
                logger.info("获得" + gameTransaction.getTransactionInfo() + "锁失败");
                gameTransactionTryCommitResult = CommitResult.LOCK_ERROR;
            }
        } catch (Exception e) {
            logger.error("提交" + gameTransaction.getTransactionInfo() + "锁异常", e);
            try {
                gameTransaction.rollback();
            } catch (Exception e2) {
                logger.error("回滚" + gameTransaction.getTransactionInfo() + "锁异常", e);
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
            logger.info("释放" + gameTransaction.getTransactionInfo() + "锁");
            gameTransaction.unlockAll();
        }

        return gameTransactionTryCommitResult;
    }

}

