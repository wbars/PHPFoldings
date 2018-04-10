<?php
class A {<fold text='...'>
    private $a;
    private $c;

    public function setA($a) {<fold text='...'>
        <fold text='a'>$this->a</fold> = $a;
    </fold>}

    public function setB($a) {<fold text='...'>
        echo "b";
    </fold>}

    public function setC($c) {<fold text='...'>
        <fold text='c'>$this->c</fold> = $c;
        return $this;
    </fold>}
</fold>}

$a = new A();

$a-><fold text='a = '>setA(</fold>"a"<fold text=''>)</fold>;
$a->setB("a");
$a->setC("a");