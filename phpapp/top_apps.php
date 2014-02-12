<?php

echo "<style>a{color:#4183c4}</style>";

$url = "http://www.github.com/nesi/applications/wiki";

$html = file_get_contents($url);

$matches = array();
preg_match("/markdown-body.*?(<ul.*?ul>)/is", $html, $matches);
$html = $matches[1];

$filtered = str_replace('href="wiki', 'href="'. $url /*$_SERVER['REQUEST_URI']*/, $html);
//$filtered = preg_replace("/<script.*?script>/is","", $filtered);
echo $filtered;

?>