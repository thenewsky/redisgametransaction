package com.redis.transaction.service;

import com.redis.log.Loggers;
import com.redis.transaction.GameTransactionImpl;
import com.redis.transaction.entity.AbstractTEntity;
import com.redis.transaction.enums.CommitResult;
import com.redis.transaction.exception.TException;
import org.slf4j.Logger;


/**
 * Created by jiangwenping on 16/12/6.
 */
public class TransactionServiceImpl implements TransactionService {


    protected final Logger logger = Loggers.transactionLogger;

    public CommitResult commitTransaction(String gameTransactionCause, AbstractTEntity... abstractGameTransactionEntity) {
        CommitResult gameTransactionTryCommitResult = CommitResult.SUCCESS;
        GameTransactionImpl gameTransaction = new GameTransactionImpl(gameTransactionCause);
        for (int i = 0; i < abstractGameTransactionEntity.length; i++) {
            gameTransaction.addEntity(abstractGameTransactionEntity[i]);
        }
        try {
            if (gameTransaction.createGameTransactionLock()) {
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
            gameTransaction.releaseGameTransactionLock();
        }

        return gameTransactionTryCommitResult;
    }


    public CommitResult commitTransaction(String gameTransactionCause, long waitTime, AbstractTEntity... abstractGameTransactionEntity) {
        CommitResult gameTransactionTryCommitResult = CommitResult.SUCCESS;
        GameTransactionImpl gameTransaction = new GameTransactionImpl(gameTransactionCause, waitTime);
        for (int i = 0; i < abstractGameTransactionEntity.length; i++) {
            gameTransaction.addEntity(abstractGameTransactionEntity[i]);
        }
        try {
            if (gameTransaction.createGameTransactionLock()) {
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
            gameTransaction.releaseGameTransactionLock();
        }

        return gameTransactionTryCommitResult;
    }

}

