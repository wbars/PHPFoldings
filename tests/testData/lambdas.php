<?php
$a = <fold text=''>function </fold>()<fold text=' -> '><fold text='...'>
{
    return </fold>1<fold text=''>;
}</fold></fold>;

$a = <fold text=''>function </fold>($a)<fold text=' -> '><fold text='...'>
{
    return </fold>1<fold text=''>;
}</fold></fold>;

$a = <fold text=''>function </fold>($a, $b)<fold text=' -> {'> {<fold text='...'></fold>
    echo "b";
    return 1;
</fold><fold text='}'>}</fold>;

$a = <fold text=''>function </fold>()<fold text=' -> {'> {</fold><fold text='...'>
    echo "b";
    echo "b";
</fold><fold text='}'>}</fold>;

$a = <fold text=''>function </fold>()<fold text=' -> {'> {</fold><fold text='...'>
    echo "b";
</fold><fold text='}'>}</fold>;