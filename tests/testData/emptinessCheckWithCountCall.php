<?php
$a = [];

$b = <fold text='notEmpty'>count</fold>($a)<fold text=''> > 0</fold>;
$b = <fold text='notEmpty'>count</fold>($a)<fold text=''> != 0</fold>;
$b = <fold text='notEmpty'>count</fold>($a)<fold text=''> !== 0</fold>;
$b = <fold text='empty'>count</fold>($a)<fold text=''> == 0</fold>;
$b = <fold text='empty'>count</fold>($a)<fold text=''> === 0</fold>;
$b = <fold text='notEmpty'>sizeof</fold>($a)<fold text=''> > 0</fold>;
$b = <fold text='notEmpty'>sizeof</fold>($a)<fold text=''> != 0</fold>;
$b = <fold text='notEmpty'>sizeof</fold>($a)<fold text=''> !== 0</fold>;
$b = <fold text='empty'>sizeof</fold>($a)<fold text=''> == 0</fold>;
$b = <fold text='empty'>sizeof</fold>($a)<fold text=''> === 0</fold>;

$b = sizeof($a, 1) === 0;
$b = count($a, 1) === 0;
$b = count($a, 1) > 1;
$b = count($a, 1) < 0;
$b = count($a, 1) > $a;
