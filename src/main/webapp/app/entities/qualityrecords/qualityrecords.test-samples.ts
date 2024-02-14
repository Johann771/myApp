import { IQualityrecords, NewQualityrecords } from './qualityrecords.model';

export const sampleWithRequiredData: IQualityrecords = {
  id: 16853,
  supplier: 'au-dessus de éloigner',
};

export const sampleWithPartialData: IQualityrecords = {
  id: 13979,
  supplier: 'mairie',
};

export const sampleWithFullData: IQualityrecords = {
  id: 28580,
  supplier: 'à travers tranquille',
  test2: 15715,
};

export const sampleWithNewData: NewQualityrecords = {
  supplier: 'élever actionnaire membre du personnel',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
