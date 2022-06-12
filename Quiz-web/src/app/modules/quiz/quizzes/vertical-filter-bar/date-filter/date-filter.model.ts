import {FBFilter} from "../fb-filter.interface";

export class DateFilter implements FBFilter {

  public filterValue: string[] = []

  currMin: Date;
  currMax: Date;

  public update(){
    this.currMin=new Date(this.filterValue[0])
    this.currMax=new Date(this.filterValue[1])
  }

  constructor(
    public identifier: string,
    public title: string,
    public min: Date,
    public max: Date,
  ) {
    this.filterValue.push(min.toDateString())
    this.filterValue.push(max.toDateString())
    min.setHours(-min.getTimezoneOffset()/60)
    max.setHours(-max.getTimezoneOffset()/60)
    this.currMin = min;
    this.currMax = max;
  }

  public getClassName(): string {
    return 'DateFilter'
  }

  public resetFilterValue(): void {
    this.filterValue = []
    this.filterValue.push(this.min.toDateString())
    this.filterValue.push(this.max.toDateString())
  }
}
