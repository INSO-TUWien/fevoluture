import {Directive, ElementRef, Input, OnChanges, Renderer2, SimpleChanges} from '@angular/core';

@Directive({
  selector: '[slFdvColor]'
})
export class ColorDirective implements OnChanges {

  @Input() color: string;

  constructor(private elementRef: ElementRef, private renderer: Renderer2) {
    this.setChildrenColor(this.elementRef.nativeElement);
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.elementRef.nativeElement.style.color = this.color;
    this.setChildrenColor(this.elementRef.nativeElement);
  }

  setChildrenColor(elementRef): void {
    this.renderer.setStyle(elementRef, 'color', this.color);
    const a = elementRef.querySelector('a');
    if (a !== null) {
      this.renderer.setStyle(a, 'color', this.color);
    }
  }
}
