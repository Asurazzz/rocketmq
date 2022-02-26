before:
```
4台机器,2m-2s 两组两从
集群中的m-s使用的sync方式

rocketmq的message.setWaitStoreMsgOK(true)代表
如果是异步要到达master，返回ok
如果是同步，master和slave都成功就返回ok

如果是false的话就约等于Kafka的 ACK=0

常识理论：
linux环境，防火墙关闭，主机名，hosts
强调：主机名不要包含下划线_     -是可以的

node01 - node04
4个配置文件
自动去nameserver上去注册
storepath路径的规划
logs文件路径的规划

/var是存储数据用的
在以下的目录中创建目录以及它的子目录：
mkdir -p /var/rocketmq/{logs,store/{commitlog,consumerqueue,index}}

```
