import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'yesOrNot'
})
export class YesOrNotPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    return (value != null && value != '' && !value) ? 'Y' : 'N';
  }

}
