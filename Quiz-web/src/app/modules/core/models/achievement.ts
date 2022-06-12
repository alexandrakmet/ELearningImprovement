import {AchievementCondition} from "./achievementCondition";

export class Achievement {

  id: number;
  name: string;
  description: string;
  conditions: AchievementCondition[]=[new AchievementCondition];
  image: any;

}
