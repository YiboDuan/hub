<?php
/*

    Input:
 
    $_GET['format'] = [ json | html | xml ]
    $_GET['method'] = [ matchby<column_name> ]
    $_GET['field'] = []

 
*/
 
/**
 * Deliver HTTP Response
 * @param string $format The desired HTTP response content type: [json, html, xml]
 * @param string $api_response The desired HTTP response data
 * @return void
 **/
function deliver_response($format, $api_response){
 
    // Define HTTP responses
    $http_response_code = array(
        200 => 'OK',
        400 => 'Bad Request',
        401 => 'Unauthorized',
        403 => 'Forbidden',
        404 => 'Not Found'
    );
 
    // Set HTTP Response
    header('HTTP/1.1 '.$api_response['status'].' '.$http_response_code[ $api_response['status'] ]);
 
    // Process different content types
    if( strcasecmp($format,'json') == 0 ){
 
        // Set HTTP Response Content Type
        header('Content-Type: application/json; charset=utf-8');
 
        // Format data into a JSON response
        $json_response = json_encode($api_response);
 
        // Deliver formatted data
        echo $json_response;
 
    }elseif( strcasecmp($format,'xml') == 0 ){
 
        // Set HTTP Response Content Type
        header('Content-Type: application/xml; charset=utf-8');
 
        // Format data into an XML response (This is only good at handling string data, not arrays)
        $xml_response = '<?xml version="1.0" encoding="UTF-8"?>'."\n".
            '<response>'."\n".
            "\t".'<code>'.$api_response['code'].'</code>'."\n".
            "\t".'<data>'.$api_response['data'].'</data>'."\n".
            '</response>';
 
        // Deliver formatted data
        echo $xml_response;
 
    }else{
 
        // Set HTTP Response Content Type (This is only good at handling string data, not arrays)
        header('Content-Type: text/html; charset=utf-8');
 
        // Deliver formatted data
        echo $api_response['data'];
 
    }
 
    // End script process
    exit;
 
}
 
// Define whether an HTTPS connection is required
$HTTPS_required = FALSE;
 
// Define whether user authentication is required
$authentication_required = FALSE;
 
// Define API response codes and their related HTTP response
$api_response_code = array(
    0 => array('HTTP Response' => 400, 'Message' => 'Unknown Error'),
    1 => array('HTTP Response' => 200, 'Message' => 'Success'),
    2 => array('HTTP Response' => 403, 'Message' => 'HTTPS Required'),
    3 => array('HTTP Response' => 401, 'Message' => 'Authentication Required'),
    4 => array('HTTP Response' => 401, 'Message' => 'Authentication Failed'),
    5 => array('HTTP Response' => 404, 'Message' => 'Invalid Request'),
    6 => array('HTTP Response' => 400, 'Message' => 'Invalid Response Format')
);
 
// Set default HTTP response of 'ok'
$response['code'] = 0;
$response['status'] = 404;
$response['data'] = NULL;
 
// --- Step 2: Authorization
 
// Optionally require connections to be made via HTTPS
if( $HTTPS_required && $_SERVER['HTTPS'] != 'on' ){
    $response['code'] = 2;
    $response['status'] = $api_response_code[ $response['code'] ]['HTTP Response'];
    $response['data'] = $api_response_code[ $response['code'] ]['Message']; 
 
    // Return Response to browser. This will exit the script.
    deliver_response($_GET['format'], $response);
}
 
// Optionally require user authentication
if( $authentication_required ){
 
    if( empty($_POST['username']) || empty($_POST['password']) ){
        $response['code'] = 3;
        $response['status'] = $api_response_code[ $response['code'] ]['HTTP Response'];
        $response['data'] = $api_response_code[ $response['code'] ]['Message'];
    }
 
    // Return an error response if user fails authentication. This is a very simplistic example
    // that should be modified for security in a production environment
    elseif( $_POST['username'] != 'foo' && $_POST['password'] != 'bar' ){
        $response['code'] = 4;
        $response['status'] = $api_response_code[ $response['code'] ]['HTTP Response'];
        $response['data'] = $api_response_code[ $response['code'] ]['Message'];
    }
 
}

function haversineGreatCircleDistance($latitudeFrom, $longitudeFrom, $latitudeTo, $longitudeTo, $earthRadius = 6378100)
{
  // convert from degrees to radians
  $latFrom = deg2rad($latitudeFrom);
  $lonFrom = deg2rad($longitudeFrom);
  $latTo = deg2rad($latitudeTo);
  $lonTo = deg2rad($longitudeTo);

  $latDelta = $latTo - $latFrom;
  $lonDelta = $lonTo - $lonFrom;

  $angle = 2 * asin(sqrt(pow(sin($latDelta / 2), 2) +
    cos($latFrom) * cos($latTo) * pow(sin($lonDelta / 2), 2)));
  return $angle * $earthRadius;
}

// define sql parameters
$hostname="localhost";
$username="threep_yibo";
$password="hitch199203";
$dbname="threep_hub";
    
if( strpos($_GET['method'],'matchby') !== false){
    $usertable="users";
    $matchcol=substr($_GET['method'], 7);
    $matchfield = "'" . $_GET['field'] . "'";
    
    $link = mysqli_connect($hostname,$username, $password) or die ("<html><script language='JavaScript'>alert('Unable to connect to database! Please try again later.'),history.go(-1)</script></html>");
    mysqli_select_db($link, $dbname);

    mysqli_real_escape_string($link, $matchcol);
    mysqli_real_escape_string($link, $matchfield);
    $query = "SELECT * FROM $usertable WHERE " . $matchcol . "=" . $matchfield;
    $return_arr = array();

    $fetch = mysqli_query($link, $query); 
    
    while ($row = mysqli_fetch_array($fetch, MYSQLI_ASSOC)) {
        $row_array['username'] = $row['username'];
        $row_array['password'] = $row['password'];
        $row_array['email'] = $row['email'];
        $row_array['confirmed'] = $row['confirmed'];
        array_push($return_arr,$row_array);
    }
    
    $response['code'] = 1;
    $response['status'] = $api_response_code[ $response['code'] ]['HTTP Response'];
    $response['data'] = $row_array;
}

elseif( strpos($_GET['method'],'gethubs') !== false){
    $table="hubs";
    $field= $_GET['field'];
    $field_arr= explode(',',$field);
    $latitude=(float)$field_arr[0];
    $longitude=(float)$field_arr[1];
    
    $link = mysqli_connect($hostname,$username, $password) or die ("<html><script language='JavaScript'>alert('Unable to connect to database! Please try again later.'),history.go(-1)</script></html>");
    mysqli_select_db($link, $dbname);

    $query = "SELECT * FROM $table";
    $return_arr = array();

    $fetch = mysqli_query($link, $query);
    
    while ($row = mysqli_fetch_array($fetch, MYSQLI_ASSOC)) {
    	$row['id'] = str_replace(' ', '', stripslashes(trim($row['id'])));
    	$row['latitude'] = str_replace(' ', '', stripslashes(trim($row['latitude'])));
    	$row['longitude'] = str_replace(' ', '', stripslashes(trim($row['longitude'])));
    	$row['creator'] = str_replace(' ', '', stripslashes(trim($row['creator'])));
    	$row['privacy'] = str_replace(' ', '', stripslashes(trim($row['privacy'])));
    	$row['radius'] = str_replace(' ', '', stripslashes(trim($row['radius'])));
    	$row['max'] = str_replace(' ', '', stripslashes(trim($row['max'])));
    	$row['password'] = str_replace(' ', '', stripslashes(trim($row['password'])));
    	$row['time'] = str_replace(' ', '', stripslashes(trim($row['time'])));
        $h_latitude = floatval($row['latitude']);
        $h_longitude = floatval($row['longitude']);
        $distance = haversineGreatCircleDistance($latitude,$longitude,$h_latitude,$h_longitude);
        if($distance < $row['radius']) {
            array_push($return_arr,$row);
        }
    }
    
    $response['code'] = 1;
    $response['status'] = $api_response_code[ $response['code'] ]['HTTP Response'];
    $response['data'] = $return_arr;
    
}

elseif( strcmp($_POST['method'],'createuser') == 0){
    $usertable="users";
    $link = mysqli_connect($hostname,$username, $password) or die ("<html><script language='JavaScript'>alert('Unable to connect to database! Please try again later.'),history.go(-1)</script></html>");
    mysqli_select_db($link, $dbname);
    
    $uac_username = $_POST['username'];
    $uac_password = $_POST['password'];
    $uac_email = $_POST['email'];
    
    mysqli_real_escape_string($link, $uac_username);
    mysqli_real_escape_string($link, $uac_password);
    mysqli_real_escape_string($link, $uac_email);
    
    $query = "INSERT INTO $usertable " . "VALUES " . "('" . $uac_username . "','" . $uac_password . "','" . $uac_email . "',0)";
    $result = mysqli_query($link, $query);
    
    if( $result === true) {
    	$result = "success";
    }
    
    $response['code'] = 1;
    $response['status'] = $api_response_code[ $response['code'] ]['HTTP Response'];
    $response['data'] = $result;
}

elseif( strcmp($_POST['method'],'createhub') == 0){
    $usertable="hubs";
    $relationtable="relations";
    $link = mysqli_connect($hostname,$username, $password) or die ("<html><script language='JavaScript'>alert('Unable to connect to database! Please try again later.'),history.go(-1)</script></html>");
    mysqli_select_db($link, $dbname);
    
    $hc_name = $_POST['name'];
    $hc_latitude = $_POST['latitude'];
    $hc_longitude = $_POST['longitude'];
    $hc_creator = $_POST['creator'];
    $hc_privacy = $_POST['privacy'];
    $hc_radius = $_POST['radius'];
    $hc_max = $_POST['max'];
    $hc_password = -1;
    
    if(strcmp($_POST['privacy'],'private') == 0) {
    	$hc_password = $_POST['password'];
    	mysqli_real_escape_string($link, $hc_password);
    }
    
    mysqli_real_escape_string($link, $hc_name);
    mysqli_real_escape_string($link, $hc_latitude);
    mysqli_real_escape_string($link, $hc_longitude);
    mysqli_real_escape_string($link, $hc_creator);
    mysqli_real_escape_string($link, $hc_privacy);
    mysqli_real_escape_string($link, $hc_radius);
    mysqli_real_escape_string($link, $hc_max);
    
    
    $query = "INSERT INTO $usertable VALUES (NULL,'$hc_name','$hc_latitude','$hc_longitude','$hc_creator','$hc_privacy','$hc_radius','$hc_max','$hc_password',NULL);";
    
    $result1 = mysqli_query($link, $query);
    
    if( $result1 === true) {
    	$result1 = "success";
    }
    
    $query = "INSERT INTO $relationtable VALUES (NULL,'$hc_name','$hc_creator',creator);";
    $result2 = mysqli_query($link, $query);
    
    if( $result2 === true) {
    	$result2 = "success";
    }
    
    if( $result1 && $result2 === true) {
        $result = true;
    } else {
    	$result = false;
    }
    
    $response['code'] = 1;
    $response['status'] = $api_response_code[ $response['code'] ]['HTTP Response'];
    $response['data'] = $result;
}

// Return Response to browser
deliver_response($_GET['format'], $response);
 
?>
