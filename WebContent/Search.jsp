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
<style type="text/css">
 #topcorner{
   position:absolute;
   top:0;
   right:0;
  }
</style>

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
        <script>
        $(document).on("click", "#search1", function() {
        	
        	$("#result").html("");
        	$.ajax({
        		  url: "SearchQuery",
        		  type: "get", //send it through get method
        		  dataType: 'json',
        		  cache:false,
        		  data: { 
        		    keyword:document.getElementById("searchform-keyword").value,
        		    precision:document.getElementById("searchform-precision").value
        		  },
        		  success: function(responseJson) {
        	
        			 var $ul = $("<ul>").appendTo($("#result"));
        		        $.each(responseJson, function(index, item) {
        		        	 $("<li>").text(item).appendTo($ul); 
        		        }); 
        		        
        		        document.getElementById('searchform-keyword').value = "";
        		        document.getElementById('searchform-precision').value = "";
        		      
        		  },
        		  error: function(xhr) {
        		    //Do Something to handle error
        		  }
        		}); 
       		
        });
        
        
        
        $(document).on("click", "#search2", function() {
        	
        	$("#result").html("");
        	$.ajax({
        		  url: "Search2Query",
        		  type: "get", //send it through get method
        		  dataType: 'json',
        		  cache:false,
        		  data: { 
        		    keyword:document.getElementById("searchform-keyword").value,
        		    precision:document.getElementById("searchform-precision").value
        		  },
        		  success: function(responseJson) {
        	
        			 var $ul = $("<ul>").appendTo($("#result"));
        		        $.each(responseJson, function(index, item) {
        		        	 $("<li>").text(item).appendTo($ul); 
        		        }); 
        		        
        		        document.getElementById('searchform-keyword').value = "";
        		        document.getElementById('searchform-precision').value = "";
        		      
        		  },
        		  error: function(xhr) {
        		    //Do Something to handle error
        		  }
        		}); 
       		
        });

        $(document).on("click", "#search3", function() {
        	
        	$("#result").html("");
        	$.ajax({
        		  url: "Search3Query",
        		  type: "get", //send it through get method
        		  dataType: 'json',
        		  cache:false,
        		  data: { 
        		    keyword:document.getElementById("searchform-keyword").value,
        		    precision:document.getElementById("searchform-precision").value
        		  },
        		  success: function(responseJson) {
        	
        			 var $ul = $("<ul>").appendTo($("#result"));
        		        $.each(responseJson, function(index, item) {
        		        	 $("<li>").text(item).appendTo($ul); 
        		        }); 
        		        
        		        document.getElementById('searchform-keyword').value = "";
        		        document.getElementById('searchform-precision').value = "";
        		      
        		  },
        		  error: function(xhr) {
        		    //Do Something to handle error
        		  }
        		}); 
        		
        }); 
        
       
        $(document).on("click", "#search4", function() {
        	
        	$("#result").html("");
        	$.ajax({
        		  url: "Search4Query",
        		  type: "get", //send it through get method
        		  dataType: 'json',
        		  cache:false,
        		  data: { 
        		    keyword:document.getElementById("searchform-keyword").value,
        		    precision:document.getElementById("searchform-precision").value
        		  },
        		  success: function(responseJson) {
        	
        			 var $ul = $("<ul>").appendTo($("#result"));
        		        $.each(responseJson, function(index, item) {
        		        	 $("<li>").text(item).appendTo($ul); 
        		        }); 
        		        
        		        document.getElementById('searchform-keyword').value = "";
        		        document.getElementById('searchform-precision').value = "";
        		      
        		  },
        		  error: function(xhr) {
        		    //Do Something to handle error
        		  }
        		}); 
       		
        });

 $(document).on("click", "#search5", function() {
        	
        	$("#result").html("");
        	$.ajax({
        		  url: "Search5Query",
        		  type: "get", //send it through get method
        		  dataType: 'json',
        		  cache:false,
        		  data: { 
        		    keyword:document.getElementById("searchform-keyword").value,
        		    precision:document.getElementById("searchform-precision").value
        		  },
        		  success: function(responseJson) {
        	
        			 var $ul = $("<ul>").appendTo($("#result"));
        		        $.each(responseJson, function(index, item) {
        		        	 $("<li>").text(item).appendTo($ul); 
        		        }); 
        		        
        		        document.getElementById('searchform-keyword').value = "";
        		        document.getElementById('searchform-precision').value = "";
        		      
        		  },
        		  error: function(xhr) {
        		    //Do Something to handle error
        		  }
        		}); 
       		
        });
   
            
            $(document).on("click", "#search6", function() {
            	
            	$("#result").html("");
            	$.ajax({
            		  url: "Search6Query",
            		  type: "get", //send it through get method
            		  dataType: 'json',
            		  cache:false,
            		  data: { 
            		    keyword:document.getElementById("searchform-keyword").value,
            		    precision:document.getElementById("searchform-precision").value
            		  },
            		  success: function(responseJson) {
            	
            			 var $ul = $("<ul>").appendTo($("#result"));
            		        $.each(responseJson, function(index, item) {
            		        	 $("<li>").text(item).appendTo($ul); 
            		        }); 
            		        
            		        document.getElementById('searchform-keyword').value = "";
            		        document.getElementById('searchform-precision').value = "";
            		      
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
                        <a href="DataOwner.jsp"><button id="topcorner" class="btn" Width="10px">Click to add  more files</button></a>
                       
                           <br>
                           <br>
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
				                     
                                         <button type="button" class="btn" id="search1" name="Technique1" Width="10px">WildCard Based Approach</button>                    
                                         <br>
                                         <br>
                      
                                         <button type="button" class="btn" id="search2" name="Technique2" Width="10px">Trie Based Approach</button>
                                          <br>
                                         <br>
                                          <button type="button" class="btn" id="search3" name="Technique3" Width="10px">WildCard Based Approach with Caching</button>
                                          <br>
                                         <br>
                                          <button type="button" class="btn" id="search4" name="Technique4" Width="10px">Trie Based Approach with Caching</button>
                                          <br>
                                         <br>
                                          <button type="button" class="btn" id="search5" name="Technique5" Width="10px">Synonym Based</button>
                                         <br>
                                         <br>
                                         <button type="button" class="btn" id="search6" name="Technique6" Width="10px">KNN Search</button>
                                          
				                    </form>
				                    
				                    
				                    <div id="result">
				                  
				                    </div>
			                   </div>
			                   
			                   
			                 
</body>
</html>