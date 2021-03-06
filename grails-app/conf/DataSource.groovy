dataSource 
{
	pooled = true
	driverClassName = "com.mysql.jdbc.Driver"
	username = "root"
	password = "jsidev"
	dbCreate = "create-drop"
	url = "jdbc:mysql://localhost/paisley"
}

hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}

// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "update" // one of 'create', 'create-drop','update'
        }
    }
    test {
        dataSource {
            dbCreate = "update"
        }
    }
    production {
        dataSource {
            dbCreate = "update"	
        }
    }
}
