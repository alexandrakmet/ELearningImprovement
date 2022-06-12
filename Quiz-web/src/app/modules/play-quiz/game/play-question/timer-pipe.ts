import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'timerFormat'
})
export class TimerPipe implements PipeTransform {

  transform(value: number, args?: any): string {
    let fsec: string = (value % 60).toString();
    let fmin: string = ((value - value % 60) / 60).toString();

    if (fsec.length < 2) {
      fsec = '0' + fsec;
    }
    if (fmin.length < 2) {
      fmin = '0' + fmin;
    }

    return fmin + ':' + fsec;
  }
}
