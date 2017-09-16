
# ping sbs-eureka-web
Invoke-WebRequest -URI http://localhost:8080/ping

# ping sbs-eureka-app
Invoke-WebRequest -URI http://localhost:9020/ping

# ping sbs-eureka-server
Invoke-WebRequest -URI http://localhost:8761/ping

# ping sbs-redis-server
Invoke-WebRequest -URI http://localhost:7379/ping

# ping sbs-mariadb-server
Invoke-WebRequest -URI http://localhost:3088/ping

# ping sbs-syslog4j-server
Invoke-WebRequest -URI http://localhost:8088/ping

$jobs = Get-Job

foreach($job in $jobs){
    if($job.State -eq "Completed") {
        Write-Host "Remove Job "$job.Id
        Remove-Job $job.Id
    }
}
