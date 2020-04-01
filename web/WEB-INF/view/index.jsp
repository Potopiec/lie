<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>File</title>
  </head>
  <body>
  <header><h3>Encrypter v 0.1</h3></header>

 <form:form method="post" enctype="multipart/form-data">

     <input type="file" name="img" />
     </br>
     <input style="height: 300px;width: 400px" type="text" name="addText"/>
     </br>
     <input type="submit" formaction="encrypt" value="encrypt"/>
     <input type="submit"  formaction="decript" value="decript"/>

 </form:form>


  </body>
</html>
