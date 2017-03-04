<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
 <link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" href="assets/font-awesome/css/font-awesome.min.css">
		<link rel="stylesheet" href="assets/css/form-elements.css">
        <link rel="stylesheet" href="assets/css/style.css">

        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
            <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->

        <!-- Favicon and touch icons -->
        <link rel="shortcut icon" href="assets/ico/favicon.png">
        <link rel="apple-touch-icon-precomposed" sizes="144x144" href="assets/ico/apple-touch-icon-144-precomposed.png">
        <link rel="apple-touch-icon-precomposed" sizes="114x114" href="assets/ico/apple-touch-icon-114-precomposed.png">
        <link rel="apple-touch-icon-precomposed" sizes="72x72" href="assets/ico/apple-touch-icon-72-precomposed.png">
        <link rel="apple-touch-icon-precomposed" href="assets/ico/apple-touch-icon-57-precomposed.png">
<title>Search File</title>


<script src="http://code.jquery.com/jquery-latest.min.js"></script>
        <script>
            $(document).on("click", "#search2", function() {
            	window.alert("clicked"+document.getElementById("searchform-keyword").value+" "+document.getElementById("searchform-precision").value);
            	// When HTML DOM "click" event is invoked on element with ID "somebutton", execute the following function...
            	$.ajax({
            		  url: "Search2Query",
            		  type: "get", //send it through get method
            		  data: { 
            		    keyword:document.getElementById("searchform-keyword").value,
            		    precision:document.getElementById("searchform-precision").value
            		  },
            		  success: function(response) {
            			 
            		        $.each(responseJson, function(index, item) { 
            		           window.alert(item+ " "+index);    
            		        });
            		  },
            		  error: function(xhr) {
            		    //Do Something to handle error
            		  }
            		});
            });
        </script>
</head>
<body>
<div class="row">
                        <div class="col-sm-8 col-sm-offset-2 text">
                            <h1>Enter Keywords</h1>
                           
                        </div>
                    </div>
                    
                     <div class="form-bottom">
				                    <form role="form" method="post" class="search-form">
				                    	<div class="form-group">
				                    		<label class="sr-only" for="form-search">Enter Keyword here</label>
				                        	<input type="text" name="keyword"  placeholder="Keyword..." class="form-search form-control" id="searchform-keyword">
				                        	<label class="sr-only" for="form-search">Enter precision here</label>
				                        	<input type="text" name="precision"  placeholder="Precision..." class="form-search form-control" id="searchform-precision">
				                        </div>
				                     
                                         <button type="submit" class="btn" id="search2" name="Technique2" Width="10px">Trie Based Approach</button>
                                         
				                    </form>
				                    
			                   </div>
			                   
			                   
			                 
</body>
</html>