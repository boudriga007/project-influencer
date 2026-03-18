import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IScoreReach, NewScoreReach } from '../score-reach.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IScoreReach for edit and NewScoreReachFormGroupInput for create.
 */
type ScoreReachFormGroupInput = IScoreReach | PartialWithRequiredKeyOf<NewScoreReach>;

type ScoreReachFormDefaults = Pick<NewScoreReach, 'id'>;

type ScoreReachFormGroupContent = {
  id: FormControl<IScoreReach['id'] | NewScoreReach['id']>;
  nbFollowers: FormControl<IScoreReach['nbFollowers']>;
  engagementRate: FormControl<IScoreReach['engagementRate']>;
  reachEstime: FormControl<IScoreReach['reachEstime']>;
  scoreReach: FormControl<IScoreReach['scoreReach']>;
  influenceur: FormControl<IScoreReach['influenceur']>;
};

export type ScoreReachFormGroup = FormGroup<ScoreReachFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ScoreReachFormService {
  createScoreReachFormGroup(scoreReach?: ScoreReachFormGroupInput): ScoreReachFormGroup {
    const scoreReachRawValue = {
      ...this.getFormDefaults(),
      ...(scoreReach ?? { id: null }),
    };
    return new FormGroup<ScoreReachFormGroupContent>({
      id: new FormControl(
        { value: scoreReachRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nbFollowers: new FormControl(scoreReachRawValue.nbFollowers),
      engagementRate: new FormControl(scoreReachRawValue.engagementRate),
      reachEstime: new FormControl(scoreReachRawValue.reachEstime),
      scoreReach: new FormControl(scoreReachRawValue.scoreReach),
      influenceur: new FormControl(scoreReachRawValue.influenceur),
    });
  }

  getScoreReach(form: ScoreReachFormGroup): IScoreReach | NewScoreReach {
    return form.getRawValue() as IScoreReach | NewScoreReach;
  }

  resetForm(form: ScoreReachFormGroup, scoreReach: ScoreReachFormGroupInput): void {
    const scoreReachRawValue = { ...this.getFormDefaults(), ...scoreReach };
    form.reset({
      ...scoreReachRawValue,
      id: { value: scoreReachRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ScoreReachFormDefaults {
    return {
      id: null,
    };
  }
}
