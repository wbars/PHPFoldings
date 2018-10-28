<?php
class A{}

$a = new A();

$b = $a instanceof A;
$b = !(true);
$b = !(true && false);
$b = !($a instanceof A && true);
$b = !(($a instanceof A && true));
$b = <fold text=''>!</fold>$a<fold text=' not '> </fold>instanceof A;
$b = <fold text=''>!</fold>$a<fold text=' not '> </fold>instanceof A && true;
$b = <fold text=''>!((</fold>$a<fold text=' not '> </fold>instanceof A<fold text=''>))</fold>;

$c = <fold text=''>!(</fold>$a<fold text=' not '> </fold>instanceof A<fold text=''>)</fold>;
$c = <fold text=''>!((</fold>$a<fold text=' not '> </fold>instanceof A<fold text=''>))</fold>;
$b = !(<fold text=''>!(</fold>$a<fold text=' not '> </fold>instanceof A<fold text=''>)</fold>);
