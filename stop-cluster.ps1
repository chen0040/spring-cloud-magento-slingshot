
# kill sbs-eureka-web
Invoke-WebRequest -URI http://localhost:8080/kill

# kill sbs-eureka-app
Invoke-WebRequest -URI http://localhost:9020/kill

# kill sbs-eureka-app-magento
Invoke-WebRequest -URI http://localhost:9022/kill

# kill sbs-eureka-server
Invoke-WebRequest -URI http://localhost:8761/kill

# kill sbs-redis-server
Invoke-WebRequest -URI http://localhost:7379/kill

# kill sbs-mariadb-server
Invoke-WebRequest -URI http://localhost:3088/kill

# kill sbs-syslog4j-server
Invoke-WebRequest -URI http://localhost:8088/kill

$jobs = Get-Job

foreach($job in $jobs){
    if($job.State -eq "Completed") {
        Write-Host "Remove Job "$job.Id
        Remove-Job $job.Id
    }
}

