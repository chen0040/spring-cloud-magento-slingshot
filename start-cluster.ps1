$clef_bin_dir=$PSScriptRoot + "/../bin"

$projs=@("sbs-mariadb-server", "sbs-redis-server", "sbs-eureka-server", "sbs-syslog4j-server", "sbs-eureka-app", "sbs-eureka-web")

foreach ($proj in $projs){
    $jarName=$clef_bin_dir + "/" + $proj + ".jar"

    Write-Host $jarName

    Start-Job -ScriptBlock { Param($one,$two,$three)
     java -jar $one 1> $HOME/clef-wazen-logs/$two-console.out 2> $HOME/clef-wazen-logs/$three-console.error
    } -ArgumentList $jarName,$proj,$proj

    Start-Sleep -s 5
}
