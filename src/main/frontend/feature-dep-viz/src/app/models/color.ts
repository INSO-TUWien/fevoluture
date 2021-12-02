export class Color {

  static COLORS: Color[] =
    [
      Color.toColor({r: 0, g: 94, b: 255, a: 1}),
      Color.toColor({r: 255, g: 102, b: 0, a: 1}),
      Color.toColor({r: 60, g: 120, b: 61, a: 1}),
      Color.toColor({r: 200, g: 0, b: 255, a: 1}),
      Color.toColor({r: 255, g: 230, b: 0, a: 1}),
    ];

  r: number;
  g: number;
  b: number;
  a: number;

  static RED(): Color {
    return Color.hexToColor('#ff0000');
  }

  static BLACK(): Color {
    return Color.hexToColor('#000');
  }

  static HIGHLIGHT(): Color {
    return Color.hexToColor('#FFFF00');
  }

  static LOGICAL_COUPLING(): Color {
    return Color.hexToColor('#69b3a2');
  }

  static EDGE(): Color {
    return Color.hexToColor('#5a5a5a');
  }

  static toColor(obj: any): Color {
    const color = new Color();
    color.r = obj?.r;
    color.b = obj?.b;
    color.g = obj?.g;
    color.a = obj?.a;
    return color;
  }

  static hexToColor(hex: string, alpha?): Color {
    const r = parseInt(hex.slice(1, 3), 16);
    const g = parseInt(hex.slice(3, 5), 16);
    const b = parseInt(hex.slice(5, 7), 16);

    const color = new Color();
    color.r = r;
    color.g = g;
    color.b = b;

    if (alpha) {
      color.a = alpha;
    } else {
      color.a = 1;
    }
    return color;
  }

  toRGBA(): string {
    return `rgba(${this.r},${this.g},${this.b},${this.a})`;
  }

  toHex(): string {
    return '#' + this.componentToHex(this.r) + this.componentToHex(this.g) + this.componentToHex(this.b);
  }

  private componentToHex(c) {
    const hex = c.toString(16);
    return hex.length === 1 ? '0' + hex : hex;
  }
}
