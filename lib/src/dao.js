var ioc = {
        dao : {
            type : "org.nutz.dao.impl.NutDao",
            args : [{refer:"dataSource"}]
        },
        dataSource : {
            type : "com.alibaba.druid.pool.DruidDataSource",
            fields : {
                jdbcUrl : 'jdbc:mysql://127.0.0.1:3306/st_work',
                username : 'root',
                password : 'root'
            }
        }
}