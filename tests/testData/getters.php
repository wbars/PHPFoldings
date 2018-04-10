<?php
class A {<fold text='...'>
    private $a;
    private $b;
    private $c;
    private $d;
    private $f;

    public function getA() {<fold text='...'>
        return <fold text='a'>$this->a</fold>;
    </fold>}

    public function getB() {<fold text='...'>
        // wrong field
        return <fold text='c'>$this->c</fold>;
    </fold>}

    public function getF($a) {<fold text='...'>
        // wrong field
        return <fold text='f'>$this->f</fold>;
    </fold>}

    public function getC() {<fold text='...'>
        // non trivial
        echo "b";
        return <fold text='d'>$this->d</fold>;
    </fold>}

    public function getD() {<fold text='...'>
        // wo return
        echo "b";
    </fold>}
</fold>}

$a = new A();
$a-><fold text='a'>getA()</fold>;
$a->getB();
$a->getC();
$a->getD();
$a->getF($a);