<?php
class A {<fold text='...'>
    private $a;
    private $b;
    private $c;
    private $d;
    private $f;

    public function getA() {<fold text='...'>
        return <fold text=''>$this-></fold>a;
    </fold>}

    public function getB() {<fold text='...'>
        // wrong field
        return <fold text=''>$this-></fold>c;
    </fold>}

    public function getF($a) {<fold text='...'>
        // wrong field
        return <fold text=''>$this-></fold>f;
    </fold>}

    public function getC() {<fold text='...'>
        // non trivial
        echo "b";
        return <fold text=''>$this-></fold>d;
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