### 命名规则

- T:Transaction缩写
- GT:GameTransaction缩写
- TJob :TransactionJob 

## 设计思路
 
 >job用于执行事务[entitys],包括一些job_entity_1,job_entity_2,...,job_entity_n

执行思路如下:
- job.commit(entity1,entity2 ...  entity_n);

### job.commit 实现如下:

1. **job.lockall()**  : job_entity_1.lock(), job_entity_2.lock()  ...... job_entity_n.lock() //锁定所有<事务entity>
2. **job.try_commit_all** :  job_entity_1.try_commit() , job_entity_2.try_commit()   ...... job_entity_n.try_commit() //检测所有 <事务entity>自身是否满足条件. 失败 return false
3. **job.can_commit_all()** : // 第二条  pass  return true
4. **job.commit_all()** :job_entity_1.commit() , job_entity_2.commit()  ...... job_entity_n.commit() // 所有 <事务entity> commit, entity内置bitset记录自身业务进度. error 发生 按照 bitset rollback 
5. **job.unlockAll()** : job_entity_1.unlock(), job_entity_2.unlock()  ...... job_entity_n.unlock() //解锁所有<事务entity>
