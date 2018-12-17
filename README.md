# okhttp
HTTP是现代应用常用的一种交换数据和媒体的网络方式，高效地使用HTTP能让资源加载更快，节省带宽。OkHttp是一个高效的HTTP客户端，它有以下默认特性：<br>
支持HTTP/2，允许所有同一个主机地址的请求共享同一个socket连接<br>
连接池减少请求延时<br>
透明的GZIP压缩减少响应数据的大小<br>
缓存响应内容，避免一些完全重复的请求<br>
当网络出现问题的时候OkHttp依然坚守自己的职责，它会自动恢复一般的连接问题，如果你的服务有多个IP地址，当第一个IP请求失败时，OkHttp会交替尝试你配置的其他IP，OkHttp使用现代TLS技术(SNI, ALPN)初始化新的连接，当握手失败时会回退到TLS 1.0。<br>

