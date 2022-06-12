import { FBFilter } from "../fb-filter.interface";

export class TagFilter implements FBFilter{

    public filterValue: string[] = []

    constructor(
        public identifier: string,
        public title: string,
        public options: string[],
    ){}

    public getClassName(): string {
        return 'TagFilter'
    }

    public resetFilterValue(): void {
        this.filterValue = []
    }

}
