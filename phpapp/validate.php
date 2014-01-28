<?php

// This file validates data in the project database, and attempts to solve issues it finds
// By Nick Young

include ('config.php');
$db = new mysqli($host, $user, $pass, $database, $port);

$researchers = $db->query("SELECT * FROM researcher")->fetch_all(MYSQLI_ASSOC);

$titles = array("A/Prof", "Prof", "Dr");
$inst = $db->query("SELECT * FROM institution")->fetch_all(MYSQLI_ASSOC);
$inst_dict = array();
$dept_dict = array();
foreach ($inst as $i) {
  $inst_dict[$i['code']] = $i['name'];
}
$inst_dict['uc'] = "University of Canterbury";
$inst_dict['auckland'] = "University of Auckland";
$inst_dict['otago uni'] = "University of Otago";
$inst_dict['otago'] = "University of Otago";
$dept_dict['mech eng'] = "Department of Mechanical Engineering";
$dept_dict['chem eng'] = "Department of Chemical and Materials Engineering";
$dept_dict['physics'] = "Department of Physics";
$dept_dict['biology'] = "School of Biological Sciences";
$dept_dict['bioeng'] = "Department of Bioengineering";
$dept_dict['bioengineering'] = "Department of Bioengineering";
// Remove titles
foreach($researchers as $r) {
  $new_name = $r['fullName'];
  foreach ($titles as $title) {
    if (strpos($new_name, $title)===0) {
      $new_name = trim(str_replace($title, "", $new_name), " .");
    }
  }
  if (preg_match("/\((.*?)\)/", $new_name, $matches)) {
    $new_name = trim(str_replace(" " . $matches[0], "", $new_name), " .");
    $additional_data = $matches[1];
    $lower_data = strtolower($additional_data);
  }
  if ($new_name!=$r['fullName']) {
    print ("Renaming " . $r['fullName'] . " to " . $new_name . "\n");
    //print "UPDATE researcher SET fullName='" . mysqli_real_escape_string($db, $new_name) . "' WHERE id=" . $r['id'];
    if (!$db->query("UPDATE researcher SET fullName='" . mysqli_real_escape_string($db, $new_name) . "' WHERE id=" . $r['id'])) die("Unable to update fullName " . $r['id']);
    if (!empty($additional_data)) {
      if (key_exists($lower_data, $inst_dict)) {
        print ("Setting " . $new_name . "'s inst to " . $inst_dict[$lower_data] . "\n");
        if (!$db->query("UPDATE researcher SET institution='" . mysqli_real_escape_string($db, $inst_dict[$lower_data]) . "' WHERE id=" . $r['id'])) die("Unable to update institution " . $r['id']);
      } else {
        foreach ($inst_dict as $key => $val) {
          if (strpos($lower_data, $key)===0) {
            print ("Setting " . $new_name . "'s inst to " . $val . "\n");
            if (!$db->query("UPDATE researcher SET institution='" . mysqli_real_escape_string($db, $val) . "' WHERE id=" . $r['id'])) die("Unable to update institution2 " . $r['id']);
            $lower_data = str_replace($key . " ", "", $lower_data);
            if (key_exists($lower_data, $dept_dict)) {
              print ("Setting " . $new_name . "'s dept to " . $dept_dict[$lower_data] . "\n");
              if (!$db->query("UPDATE researcher SET department='" . mysqli_real_escape_string($db, $dept_dict[$lower_data]) . "' WHERE id=" . $r['id'])) die("Unable to update department " . $r['id']);
            }
          }
        }
        print ("Appending " . $additional_data . " to " . $new_name . "'s notes\n");
        //print "UPDATE researcher SET notes='" . $r['notes'] . "<br>" . mysqli_real_escape_string($db, $additional_data) . "' WHERE id=" . $r['id'];
        if (!empty($r['notes'])) {
          $additional_data = $r['notes'] . "<br>" . $additional_data;
        }
        if (!$db->query("UPDATE researcher SET notes='" . mysqli_real_escape_string($db, $additional_data) . "' WHERE id=" . $r['id'])) die("Unable to update notes " . $r['id']);
      }
    }
    $additional_data = "";
    print "\n";
  }
}

//Find dupes

$researchers = $db->query("SELECT * FROM researcher")->fetch_all(MYSQLI_ASSOC);
$done = array();

foreach ($researchers as $r) {
  $name = $r['fullName'];
  if (!in_array($name, $done)) {
    $sql = "SELECT * FROM researcher WHERE fullName LIKE \"" . mysqli_real_escape_string($db, $name) . "%\"\n";
    //print $sql;
    $dr = $db->query($sql)->fetch_all(MYSQLI_ASSOC);
    if (count($dr)>1) {
      $dupes = count($dr)-1;
      foreach ($dr as $d) {
        if ($d['id']==$r['id']) continue;
        foreach ($d as $key => $val) {
          if (empty($r[$key]) || $r[$key] == "https://cluster.ceres.auckland.ac.nz/project_management/pics/avatar.gif") {
            if (!empty($d[$key]) && $d[$key]!="https://cluster.ceres.auckland.ac.nz/project_management/pics/avatar.gif") {
              $sql = "UPDATE researcher SET $key='" . mysqli_real_escape_string($db, $val) . "' WHERE id=" . $r['id'];
              if (!$db->query($sql)) die("Unable to update $key=$val");
              print "Updated $key to $val for " . $name . "\n";
            }
          }
        }
        if (!$db->query("UPDATE researcher_project SET researcherId=" . $r['id'] . " WHERE researcherId=" . $d['id'])) die("Unable to reassign projects");
        if ($db->affected_rows>0) print "Reassigned " . $db->affected_rows . " projects to " . $name . "\n";
        if (!$db->query("UPDATE researcher_project SET researcherId=" . $r['id'] . " WHERE researcherId=" . $d['id'])) die("Unable to reassign projects");
        if (!$db->query("DELETE FROM researcher WHERE id=" . $d['id'])) die("Unable to delete " . $d['id']);
      }
      print $name . " has $dupes duplicates, merged\n";
      $done[] = $name;
    }
  }
}

$pids = $db->query("SELECT id FROM project")->fetch_all();
foreach ($pids as $pid) {
  $pid = $pid[0];
  $result = $db->query("SELECT * FROM adviser_project WHERE projectId=" . $pid);
  if ($db->affected_rows==0) {
    print "Setting pa for $pid\n";
    if (!$db->query("INSERT INTO adviser_project VALUES (10, $pid, 1, '')")) die("Unable to set adviser");
  }
  $result = $db->query("SELECT * FROM project_facility WHERE projectId=" . $pid);
  if ($db->affected_rows==0) {
    print "Setting fac for $pid\n";
    if (!$db->query("INSERT INTO project_facility VALUES ($pid, 7)\n")) die("Unable to set fac");
  }
}

$db->query("UPDATE researcher SET statusId=6 WHERE startDate=''");
?>
