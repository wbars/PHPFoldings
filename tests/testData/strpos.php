<?php
$a = <fold text=''>false !== stripos(</fold>"aaa"<fold text=' contains '>, </fold>"b"<fold text=''>)</fold>;

$a = false != strpos("aaa", "b");
$a = <fold text=''>false !== strpos(</fold>"aaa"<fold text=' contains '>, </fold>"b"<fold text=''>)</fold>;
$a = false == strpos("aaa", "b");
$a = <fold text=''>false === strpos(</fold>"aaa"<fold text=' does not contain '>, </fold>"b"<fold text=''>)</fold>;
$a = 0 == strpos("aaa", "b");
$a = <fold text=''>0 === strpos(</fold>"aaa"<fold text=' starts with '>, </fold>"b"<fold text=''>)</fold>;
$a = 0 != strpos("aaa", "b");
$a = 0 !== strpos("aaa", "b");
$a = 1 == strpos("aaa", "b");
$a = 1 === strpos("aaa", "b");
$a = 1 != strpos("aaa", "b");
$a = 1 !== strpos("aaa", "b");

if (<fold text=''>strpos(</fold>"aaa"<fold text=' contains '>, </fold>"b"<fold text=''>)</fold>) {<fold text='...'>

</fold>}

if (<fold text=''>!strpos(</fold>"aaa"<fold text=' does not contain '>, </fold>"b"<fold text=''>)</fold>) {<fold text='...'>

</fold>}