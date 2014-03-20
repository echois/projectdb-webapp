<?php

use Behat\Behat\Context\ClosuredContextInterface,
    Behat\Behat\Context\TranslatedContextInterface,
    Behat\Behat\Context\BehatContext,
    Behat\Behat\Exception\PendingException;
use Behat\Gherkin\Node\PyStringNode,
    Behat\Gherkin\Node\TableNode;
    
use Behat\MinkExtension\Context\MinkContext;

//
// Require 3rd-party libraries here:
//
//   require_once 'PHPUnit/Autoload.php';
//   require_once 'PHPUnit/Framework/Assert/Functions.php';
//

/**
 * Features context.
 */
class FeatureContext extends MinkContext
{
    /**
     * Initializes context.
     * Every scenario gets it's own context object.
     *
     * @param array $parameters context parameters (set them up through behat.yml)
     */
    public function __construct(array $parameters)
    {
      // Initialize your context here
      $this->users = $parameters['users'];
      $this->root = $parameters['base_url'];
    }

    /**
   * @Given /^I fill in the following <formdetails>$/
   *
   * http://www.whiteoctober.co.uk/blog/2012/09/12/behat-tablenodes-the-missing-manual/
   */
  public function iFillInTheFollowingFormdetails(TableNode $table) {
    
    $tableValues = $table->getHash();
    $element = $this->getSession()->getPage();
    if (empty($element)) {
      throw new Exception('Page not found');
    }

    foreach($tableValues as $formData) {
      
      switch($formData['field_type']) {
      
        case 'text':
            $element->fillField($formData['form_id'], $formData['value']);
          break;
        case 'check':
        case 'select':
          print $element->selectFieldOption($formData['form_id'], $formData['value']);
          break;

      }
    }
  }
  /**
   * @Then /^I wait (\d+) ms$/
   */
  public function iWaitMs($arg1) {
    sleep($arg1/1000.0);
  }
  
  /**
   * @Then /^I click "([^"]*)"$/
   */
  public function iClick($arg1)
  {
    $element = $this->getSession()->getPage();
    $button = $element->findButton($arg1);
    $button->click();
    //$redir = $button->getValue();
    //parent::visit($redir);
  }
  
  /**
  * @Then /^I wait until I see "([^"]*)"$/
  */
  public function iWaitUntilISee($arg1)
  {
    $timeWaited = 0;
    while ($timeWaited < 10) {
      if ($this->getSession()->getPage()->hasContent($arg1)) {
        return;
      } else {
        $timeWaited++;
        sleep(1);
      }
    }
    throw new Exception("Timed out waiting for $arg1 to appear");
  }
  
  /**
   * @When /^I\'m logged in as ([^"]*)$/
   */
  public function iMLoggedInAs($arg1)
  {
    $user = $this->users[$arg1];
    $this->getSession()->setRequestHeader("RemoteUser",$user);
  }
  
  public function fetch_json($resource, $method = "GET", $postdata = "") {
    $u = "nyou045@auckland.ac.nz";
    // Create a stream
    $opts = array(
      'http'=>array(
        'method'=> $method,
        'ignore_errors' => true,
        'content' => $postdata,
        'header'=>"Accept: application/json\r\n" .
                  "Content-Type: application/json\r\n" .
                  "RemoteUser: $u\r\n" .
                  "User-Agent: drupal\r\n"
      )
    );
    $context = stream_context_create($opts);
    $url = $this->root . $resource;
    //print $url;
    //print $postdata;
    $this->response = file_get_contents($url, false, $context);
    if (is_numeric($this->response)) $this->id=$this->response;
    return $this->response;
  }
  
  /**
   * @When /^I "([^"]*)" "([^"]*)" to "([^"]*)"$/
   */
  public function iPostTo($method, $json, $endpoint)
  {
    $json = str_replace("'", '"', $json);
    if (strpos($endpoint, "id")) {
      if (isset($this->id)) {
        $endpoint = str_replace("id", $this->id, $endpoint);
        print "Substituted id for " . $this->id;
      } else {
        throw new Exception("Unable to substitute id - id not set!");
      }
    }
    $this->fetch_json($endpoint, $method, $json);
    
  }
  
  /**
  * @Then /^the response contains "([^"]*)"$/
  */
  public function theResponseContains($arg1)
  {
    if (strpos($this->response, $arg1)===false) {
      throw new Exception("$arg1 was not found in " . $this->response);
    }
  }
  
  /**
  * @Then /^the response doesn't contain "([^"]*)"$/
  */
  public function theResponseDoesntContain($arg1)
  {
    if (strpos($this->response, $arg1)!==false) {
      throw new Exception("$arg1 was found in the response");
    }
  }
  
  /**
   * @Then /^the response is JSON$/
   */
  public function theResponseIsJson()
  {
    $data = json_decode($this->response);

    if (empty($data)) {
      throw new Exception("Response was not JSON\n" . $this->response);
    }
  }
  
  /**
   * @When /^I print last response$/
   */
  public function iPrintLastResponse()
  {
    print $this->response;
  }
  
  /**
  * @When /^I load the "([^"]*)" with name "([^"]*)"$/
  */
  public function iLoadTheWithName($arg1, $arg2)
  {
    $json = $this->fetch_json($arg1 . "s/");
    $list = json_decode($json);
    foreach ($list as $a) {
      if (isset($a->fullName) && $a->fullName==$arg2 || isset($a->name) && $a->name==$arg2) {
        $this->id = $a->id;
        return;
      }
    }
    throw new Exception("No $arg1 with name $arg2");
  }
}