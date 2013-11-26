<?php

// Adviser specific ics calendar file
// By Nick Young

if(!isset($_GET['id'])) {
  echo "No id specified";
  die;
}

include ('config.php');

date_default_timezone_set("Pacific/Auckland");

$db = new mysqli($host, $user, $pass, $database, $port);

$id = $db->real_escape_string($_GET['id']);
$name = $db->query("SELECT fullName FROM adviser WHERE id=$id")->fetch_row()[0];

// We'll be outputting an ICS
header('Content-type: text/calendar');

// It will be called CeR_$name.ics
header("Content-Disposition: attachment; filename='CeR_$name.ics'");

echo "BEGIN:VCALENDAR
VERSION:2.0
PRODID:-//Center for e-Research//$name's projects//EN
CALSCALE:GREGORIAN
METHOD:PUBLISH
X-WR-CALNAME:CeR $name's projects
X-WR-TIMEZONE:Pacific/Auckland
X-WR-CALDESC:Follow Up dates and Review dates
";

$projects = $db->query("SELECT * FROM project p INNER JOIN adviser_project ap ON p.id=ap.projectId WHERE ap.adviserId=$id");

while ($p = $projects->fetch_object()) {
  $review = new DateTime($p->nextReviewDate);
  $followUp = new DateTime($p->nextFollowUpDate);
  $now = new DateTime();
  
  $review = $review->format('Ymd');
  $followUp = $followUp->format('Ymd');
  $now = $now->format('Ymd\THis');
  
  $pname = $p->name;
  // Strip commas
  $pname = str_replace(',','',$pname);
  //$desc = $p->description;
  // Remove non-printable characters
  //$desc = preg_replace( '/[^[:print:]]/', '',$desc);
  $pid = $p->id;
  //$desc .= "<br/><a href='http://cluster.ceres.auckland.ac.nz/pm/html/viewproject?id=$pid'>http://cluster.ceres.auckland.ac.nz/pm/html/viewproject?id=$pid</a>";
  $desc = "<a href='http://cluster.ceres.auckland.ac.nz/pm/html/viewproject?id=$pid'>http://cluster.ceres.auckland.ac.nz/pm/html/viewproject?id=$pid</a>";
  
  if (!empty($p->nextReviewDate)) {
    echo "BEGIN:VEVENT
DTSTART;VALUE=DATE:$review
DTEND;VALUE=DATE:$review
DTSTAMP:$now
CREATED:$now
DESCRIPTION:$desc
LAST-MODIFIED:$now
SUMMARY:$pname review date
END:VEVENT
";
  }
  if (!empty($p->nextFollowUpDate)) {
    echo "BEGIN:VEVENT
DTSTART;VALUE=DATE:$followUp
DTEND;VALUE=DATE:$followUp
DTSTAMP:$now
CREATED:$now
DESCRIPTION:$desc
LAST-MODIFIED:$now
SUMMARY:$pname follow up date
END:VEVENT
";
  }
}

echo 'END:VCALENDAR';

?>