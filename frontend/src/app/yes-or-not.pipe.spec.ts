import { YesOrNotPipe } from './yes-or-not.pipe';

describe('YesOrNotPipe', () => {
  it('create an instance', () => {
    const pipe = new YesOrNotPipe();
    expect(pipe).toBeTruthy();
  });
});
