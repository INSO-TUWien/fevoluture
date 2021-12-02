import {Directive, ElementRef, Input, OnChanges, Renderer2, SimpleChanges} from '@angular/core';

@Directive({
  selector: '[slFdvColorBackground]'
})
export class ColorBackgroundDirective implements OnChanges{

  @Input() color: string;

  constructor(private elementRef: ElementRef, private renderer: Renderer2) {
    this.elementRef.nativeElement.style.backgroundColor = this.color;
  }

  ngOnChanges(changes: SimpleChanges): void {
    // this.elementRef.nativeElement.style.backgroundColor = this.color;
    this.renderer.setStyle(this.elementRef.nativeElement, 'background-color', this.color);
  }

}
