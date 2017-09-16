$clef_bin_dir=$PSScriptRoot + "/bin"

$projs=@("sbs-mariadb-server", "sbs-redis-server", "sbs-eureka-server", "sbs-syslog4j-server", "sbs-eureka-app", "sbs-eureka-app-magento", "sbs-eureka-web")

foreach ($proj in $projs){
    $jarName=$clef_bin_dir + "/" + $proj + ".jar"

    Write-Host $jarName

    Start-Job -ScriptBlock { Param($one,$two,$three)
     java -jar $one
    } -ArgumentList $jarName

    Start-Sleep -s 5
}
