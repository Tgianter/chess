# chess
微信小程序-中国象棋后端

后端技术栈：springboot、mybatis、websocket、redis、docker、mysql、linux

特别说明：1、websocket是用来实现后端主动向前端发送数据，而不需要前端发送请求来后端取数据
         2、docker是用来实现将本地的web应用快速的部署到linux服务器上

实现功能：1、玩家的授权登录
         2、玩家匹配
         3、取消匹配
         4、向对手发送我方的走子位置信息
         5、向对手发起投降
