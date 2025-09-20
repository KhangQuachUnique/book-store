<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Test Promotion</title>
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<h2>Nhập mã khuyến mãi</h2>

<form id="promotionForm">
  <input type="text" id="code" name="code" placeholder="Nhập mã..." required>
  <button type="submit">Áp dụng</button>
</form>

<pre id="result" style="margin-top:20px; background:#f5f5f5; padding:10px;"></pre>

<script>
  $("#promotionForm").submit(function(event) {
    event.preventDefault();

    $.ajax({
      url: "<%= request.getContextPath() %>/api/apply-promotion",
      type: "POST",
      data: { code: $("#code").val() },
      success: function(response) {
        $("#result").text(JSON.stringify(response, null, 4));
      },
      error: function(xhr) {
        $("#result").text("❌ Lỗi khi gọi API: " + xhr.status);
      }
    });
  });
</script>
</body>
</html>
