<?php

function get_content($URL){
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_URL, $URL);
    $data = curl_exec($ch);
    curl_close($ch);
    return $data;
}

function startsWith($haystack, $needle)
{
    return $needle === "" || strpos($haystack, $needle) === 0;
}

function joinPaths() {
    $args = func_get_args();
    $paths = array();
    foreach ($args as $arg) {
        $paths = array_merge($paths, (array)$arg);
    }

    $paths = array_map(create_function('$p', 'return trim($p, "/");'), $paths);
    $paths = array_filter($paths);
    return join('/', $paths);
}

$org_url = substr($_GET['l'], 1);
$url = substr($_GET['l'], 1);
$html = get_content($url);

$dom = new DOMDocument;
$dom->loadHTML($html);
$xpath = new DOMXPath($dom);

$exps = array(	"//div[@class='box']/img/@src",
				"//meta[@name='twitter:image:src']/@content",
				"//meta[@property='og:image']/@content",
				"//link[@rel='image_src']/@href");

// at almost search 3 pages
for ($i = 1; $i <= 3; $i++) {

	foreach ($exps as $exp) {

		$srcNodes = $xpath->query($exp);

		if(count($srcNodes) > 0) {

			$name = $srcNodes->item(0)->value;

			if($name != "") {

				$isColon = !(strrpos($name, ":") === false);
				$isDoubleSlash = startsWith($name, "//");

				if($isDoubleSlash) {
					$name = 'http:'.$name;
				}

				if(!$isColon && !$isDoubleSlash) {
					$rel_path = substr(substr($url, 0, strrpos($url, "/")), strlen($url));
					$name = joinPaths($rel_path, $name);
				}

				break;
			}
		}
	}

	if($name != "") {
		break;
	} else {
		$srcNodes = $xpath->query("//a/@href");

		foreach($srcNodes as $srcNode) {
			if($srcNode->value != $url) {
				$url = $srcNode->value;
				break;
			}
		}
	}
}

if($name != "") {
	// send the right headers
	header("Content-Type: image/jpg");
	// dump the picture and stop the script
	//readfile($name);
	print file_get_contents($name);
} else {
	print 'org '.$org_url.' ;last '.$url;
}