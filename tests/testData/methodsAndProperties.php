<?php
class A {<fold text='...'>
    public $b;
    public function aa() {}
</fold>}

class B {<fold text='...'>
    protected $a;
    public function aa() {<fold text='...'>
        <fold text='a'>$this->a</fold>;
        <fold text='bb'>$this->bb</fold>();

        $a = new A();
        $a->aa();
        $a->b;
    </fold>}
    public function bb() {}
</fold>}