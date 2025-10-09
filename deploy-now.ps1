# Quick Deploy Script
Write-Host '==================================' -ForegroundColor Cyan
Write-Host '   BookieCake Quick Deploy' -ForegroundColor Cyan
Write-Host '==================================' -ForegroundColor Cyan
Write-Host ''

# Prompt for Tomcat path
$tomcatPath = Read-Host 'Enter Tomcat path (e.g., C:\apache-tomcat-10.1.28)'

if (-not (Test-Path "$tomcatPath\webapps")) {
    Write-Host 'Error: Invalid Tomcat path!' -ForegroundColor Red
    exit 1
}

# Stop Tomcat (if running)
Write-Host 'Stopping Tomcat...' -ForegroundColor Yellow
if (Test-Path "$tomcatPath\bin\shutdown.bat") {
    Start-Process -FilePath "$tomcatPath\bin\shutdown.bat" -WindowStyle Hidden -Wait
    Start-Sleep -Seconds 3
}

# Remove old deployment
Write-Host 'Removing old deployment...' -ForegroundColor Yellow
Remove-Item "$tomcatPath\webapps\BookieCake*" -Recurse -Force -ErrorAction SilentlyContinue

# Copy WAR
Write-Host 'Deploying BookieCake.war...' -ForegroundColor Yellow
Copy-Item 'target\BookieCake.war' "$tomcatPath\webapps\" -Force

# Start Tomcat
Write-Host 'Starting Tomcat...' -ForegroundColor Yellow
Start-Process -FilePath "$tomcatPath\bin\startup.bat"

Write-Host ''
Write-Host '==================================' -ForegroundColor Green
Write-Host '   Deployment Complete!' -ForegroundColor Green
Write-Host '==================================' -ForegroundColor Green
Write-Host ''
Write-Host 'Wait 15 seconds, then access:' -ForegroundColor White
Write-Host '  http://localhost:8080/BookieCake/user/cart' -ForegroundColor Cyan
Write-Host ''

# Open browser
Start-Sleep -Seconds 15
Start-Process 'http://localhost:8080/BookieCake/'
