<?php

$a = [];
$b = 1;

<fold text=''>array_push(</fold>$a<fold text='[] = '>, </fold>$b<fold text=''>)</fold>;
array_push($a, $b, $b);
array_push($a);
array_push();