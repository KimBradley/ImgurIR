<!DOCTYPE html>
<html lang="en">
    <head>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/mystyle.css" rel="stylesheet">
        <script src="js/jquery-2.1.0.min.js"></script>
        <script>
            $(document).ready(function () {
                $("#searchform").submit(function (e) {
                    e.preventDefault();
                    $.get("ajax/getimages.php", $("#searchform").serialize(),
                    function (resp) {
                        $("#output").html(resp);
                    });
                });
            });
        </script>
        <meta charset="utf-8" />
        <title></title>
    </head>
    <body>
            <!-- Begin page content -->
    <div class="container">
        <h1>ImgurIR</h1>
        <form id="searchform" class="form-inline" role="form">
            <div class="form-group">
                <label for="searchbar" class="sr-only">Search</label>
                <input id="searchbar" name="searchbar" type="text" class="form-control"/>
                
            </div>
            <button type="submit" class="btn btn-primary">Search</button>
       </form>
    </div>
    <div class="container" id="output">
    </div>

  
    </body>
</html>
