<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login Application</title>
</head>
<body>
    <h1>Login Application</h1>
    
    <p>Active Users: <span id="activeUserCount">Loading...</span></p>

    <script>
        function fetchActiveUserCount() {
            // Use AJAX to fetch the active user count from the server
            var xhr = new XMLHttpRequest();
            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    document.getElementById("activeUserCount").textContent = xhr.responseText;
                }
            };
            xhr.open("GET", "/my-servlet", true); // Replace with your server endpoint
            xhr.send();
        }

        // Initially fetch the active user count
        fetchActiveUserCount();

        // Refresh the active user count every 1 minute
        setInterval(fetchActiveUserCount, 60000);
    </script>

</body>
</html>