<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>

</head>
<body>
<div>
    <button onclick="javascript:getText();">getText</button>
</div>
<div>
    <button onclick="javascript:getImage();">getImage</button>
</div>
<img id="imgObj" src="">
</body>
<script src="webjars/jquery/3.3.1/jquery.min.js"></script>
<script type="text/javascript">
    function getText(){
        $.ajax({
            url : "/getText",
            method : "GET",
            success : function(data){
                console.log(data);
            },
            error : function(data){
                console.log(data);
            }
        });
    }
    function getImage(){
        $.ajax({
            url : "/getImage",
            method : "GET",
            success : function(data){
                console.log(data);
                $("#imgObj").attr("src","data:image/jpeg;base64,"+data);
            },
            error : function(data){
                console.log(data);
            }
        });
    }
</script>
</html>