#!/usr/local/bin/php -q
<?php
error_reporting(E_ALL);

/* Allow the script to hang around waiting for connections. */
set_time_limit(0);

/* Turn on implicit output flushing so we see what we're getting
 * as it comes in. */
ob_implicit_flush();

$address = '69.27.112.170';
$port = 9999;

// define sql parameters
$hostname="localhost";
$username="threep_yibo";
$password="hitch199203";
$dbname="threep_hub";

$relation_id = array();
$client_usernames = array();

if (($sock = socket_create(AF_INET, SOCK_STREAM, SOL_TCP)) === false) {
    echo "socket_create() failed: reason: " . socket_strerror(socket_last_error()) . "\n";
}

if (!socket_set_option($sock, SOL_SOCKET, SO_REUSEADDR, 1)) {
    echo 'Unable to set option on socket: '. socket_strerror(socket_last_error()) . PHP_EOL;
}

if (socket_bind($sock, $address, $port) === false) {
    echo "socket_bind() failed: reason: " . socket_strerror(socket_last_error($sock)) . "\n";
}

if (socket_listen($sock, 5) === false) {
    echo "socket_listen() failed: reason: " . socket_strerror(socket_last_error($sock)) . "\n";
}

$clients = array($sock);

do {
 	// create a copy, so $clients doesn't get modified by socket_select()
        $read = $clients;
        
        // get a list of all the clients that have data to be read from
        // if there are no clients with data, go to next iteration
        if (@socket_select($read, $write = NULL, $except = NULL, 0) < 1)
            continue;
        
        // check if there is a client trying to connect
        if (in_array($sock, $read)) {
            // accept the client, and add him to the $clients array
            $clients[] = $newsock = socket_accept($sock);
            // receive username and hub from connecting client
            $client_info = explode(",",trim(@socket_read($newsock, 1024, PHP_NORMAL_READ)));
            $username = $client_info[0];
            // use id since hubs can be the same name
            $hubID = $client_info[1];
            $table="relations";
    
	    $link = mysqli_connect($hostname,$username, $password) or die ("<html><script language='JavaScript'>alert('Unable to connect to database! Please try again later.'),history.go(-1)</script></html>");
	    mysqli_select_db($link, $dbname);
	    
	    //check whether user is already registered as a member or creator of this hub
	    $query = "SELECT * $table WHERE hub = $hub AND username = $username";
	    $result = mysqli_query($link, $query);
	    // if not, create relation between hub and user
	    if ($result !== false) {
                 $query = "INSERT INTO $table VALUES (NULL, $hubID, $username, member)";
                 mysqli_query($link, $query);
	    }
            // send the client a welcome message
            //socket_write($newsock, "Welcome $username.\n".
            //"There are ".(count($clients) - 1)." people in this hub.");
            
            socket_getpeername($newsock, $ip);
            echo "New client connected: {$ip}: $username\n";
            // map ip to user
            $client_usernames["$ip"] = $username;
            // remove the listening socket from the clients-with-data array
            $key = array_search($sock, $read);
            unset($read[$key]);
        }
        
        // loop through all the clients that have data to read from
        foreach ($read as $read_sock) {
            // read until newline or 1024 bytes
            // socket_read while show errors when the client is disconnected, so silence the error messages
            $msg = @socket_read($read_sock, 1024, PHP_NORMAL_READ);
            
            // check if the client is disconnected
            if ($msg === false) {
                // remove client for $clients array
                $key = array_search($read_sock, $clients);
                unset($clients[$key]);
                echo "client disconnected.\n";
                // continue to the next client to read from, if any
                continue;
            }
            
            // trim off the trailing/beginning white spaces
            $msg = trim($msg);
            
            // check if there is any data after trimming off the spaces
            if (!empty($msg)) {
            	
            	$msgpack = explode(":",$msg);
		$hubtag = $msgpack[0];		// hub tag sent with the message, use colon as a delimiter
		// acquire list of relations relevant to the hub msg
		$query = "SELECT * FROM relations WHERE hubid = $hubtag";
		$relationlist= array();
		if($result = mysqli_query($link, $query)) {
			while ($row = mysqli_fetch_assoc($result)) {
				$uname = $row["username"];	// key
				$innerArray = array();		// initialize temp array
				if($relationlist["$uname"] !== NULL) {		// if something is mapped to the key (username), there is already an array
					$innerArray = $relationlist["$uname"];	// get the array
				}
				array_push($innerArray, $row);			// push the row into the new array
				$relationlist["$uname"] = $innerArray;		// set mapping to modified array
			}
		}
		    
                // send this to all the clients in the $clients array (except the first one, which is a listening socket)
                foreach ($clients as $send_sock) {
                
                    // if its the listening sock or the client that we got the message from, go to the next one in the list
                    if ($send_sock == $sock || $send_sock == $read_sock)
                        continue;
		    
		    
		    socket_getpeername($send_sock, $ip)
		    $recipient_name = $client_usernames["$ip"];		// acquire recipient username
		    // check whether the recipient is "related" to the hub
		    if ($relationlist["$recipient_name"] !== NULL) {
		    	socket_write($send_sock, $msgpack[1]."\n");		// if so, send the message
		    }
                    
                } // end of broadcast foreach
                
            }
            
        } // end of reading foreach
} while (true);

socket_close($sock);
?>
